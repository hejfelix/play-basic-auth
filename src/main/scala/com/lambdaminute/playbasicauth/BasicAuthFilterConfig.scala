package com.lambdaminute.playbasicauth

case class User(name: String, password: String) {
  def toColonSeparatedString = s"$name:$password"
}

case class BasicAuthFilterConfig(userCredentials: List[User], realm: String, freePaths: List[String])
