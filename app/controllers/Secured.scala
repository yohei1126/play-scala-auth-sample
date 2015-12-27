package controllers

import play.api.mvc._
import models.User
import dao.UserDAO

trait Secured {
  private def sessionId(request: RequestHeader) = request.session.get(Security.username)

  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)

  def withAuth(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(sessionId, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }

  def withUser(f: User => Request[AnyContent] => Result) = withAuth { username => implicit request =>
    UserDAO.findOneByUsername(username).map { user =>
      f(user)(request)
    }.getOrElse(onUnauthorized(request))
  }
}