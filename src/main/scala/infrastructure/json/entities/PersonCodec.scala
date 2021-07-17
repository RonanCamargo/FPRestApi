package infrastructure.json.entities

import infrastructure.json.CirceConfiguration
import infrastructure.persistence.entities.PersonRow
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

object PersonCodec extends CirceConfiguration {
  implicit val PersonEncoder: Encoder[PersonRow] = deriveEncoder[PersonRow]
  implicit val PersonDecoder: Decoder[PersonRow] = deriveDecoder[PersonRow]
}
