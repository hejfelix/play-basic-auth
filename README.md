# play-basic-auth

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.lambdaminute/play-basic-auth_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.lambdaminute/play-basic-auth_2.12)
[![Build Status](https://travis-ci.org/hejfelix/play-basic-auth.svg?branch=master)](https://travis-ci.org/hejfelix/play-basic-auth)


[![forthebadge](http://forthebadge.com/images/badges/uses-badges.svg)](http://forthebadge.com)
[![forthebadge](http://forthebadge.com/images/badges/built-with-wordpress.svg)](http://forthebadge.com)
[![forthebadge](http://forthebadge.com/images/badges/contains-technical-debt.svg)](http://forthebadge.com)
[![forthebadge](http://forthebadge.com/images/badges/60-percent-of-the-time-works-every-time.svg)](http://forthebadge.com)

# A basic auth filter for play


## usage:
```scala
//MyApplicationLoader.scala
package controllers

import com.lambdaminute.playbasicauth.{BasicAuthFilter, User}
import play.api.ApplicationLoader.Context
import play.api._
import router.Routes

class MyApplicationLoader extends ApplicationLoader {
  def load(context: Context) = {
    new MyComponents(context).application
  }
}

class MyComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
    with AssetsComponents {

  val httpFilters = Seq(
    new BasicAuthFilter(List(User(name = "Horse", password = "carrot")), realm = "everything"))

  val homeController = new HomeController(controllerComponents)

  lazy val router = new Routes(httpErrorHandler, homeController, assets)

}

```

```
//application.conf
play.application.loader=controllers.MyApplicationLoader
```
