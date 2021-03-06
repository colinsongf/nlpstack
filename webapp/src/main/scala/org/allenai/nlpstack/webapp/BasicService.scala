package org.allenai.nlpstack.webapp

import spray.routing._

trait BasicService extends HttpService {
  val staticContentRoot = "public"

  // format: OFF
  val basicRoute =
    path("") {
      get {
        getFromFile(staticContentRoot + "/index.html")
      }
    } ~
    pathPrefix("info") {
      // TODO: version route
      path("name") {
        get {
          complete(Nlpweb.name)
        }
      }
    } ~
    get {
      unmatchedPath { p => getFromFile(staticContentRoot + p) }
    }
  // format: ON
}
