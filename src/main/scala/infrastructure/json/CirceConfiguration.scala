package infrastructure.json

import io.circe.generic.extras.Configuration

trait CirceConfiguration {
  implicit val config: Configuration =
    Configuration.default.withSnakeCaseMemberNames
}
