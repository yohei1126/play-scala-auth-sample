package controllers

import javax.inject.Inject

import dao.UserDAO
import models.User
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

class Application @Inject()(val messagesApi: MessagesApi) extends Controller with Secured with I18nSupport {

  val loginForm = Form(
    tuple(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => UserDAO.validate(User(email, password))
    })
  )

  def authenticate = Action { implicit request =>
    request.session.get(Security.username).map { user =>
      Redirect(routes.Application.index).withNewSession.flashing()
    }.getOrElse {
      loginForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.login(formWithErrors)),
        user => Redirect(routes.Application.index).withSession(Security.username -> user._1)
      )
    }
  }

  def index = withUser { user => implicit request =>
    Ok(views.html.index(user.email))
  }

  def index2 = withUser { user => implicit request =>
    val email = user.email
    Ok(views.html.index2(email))
  }

  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  def logout = Action {
    Redirect(routes.Application.login).withNewSession.flashing(
      "success" -> "You are now logged out."
    )
  }
}
