package ml.empee.templateplugin.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/** Class used for building a mongodb client. **/

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MongoUtils {

  /** Build an instance of mongodb client. **/
  public static MongoClient buildMongoClient(
      String username, String password, String host,
      boolean useSrv
  ) {
    String connectionString = "mongodb";
    if (useSrv) {
      connectionString += "+srv";
    }
    connectionString += "://" + username + ":" + password + "@" + host;
    connectionString += "/?retryWrites=true&w=majority";

    MongoClientSettings settings = MongoClientSettings.builder()
        .codecRegistry(buildCodecRegistry())
        .applyConnectionString(new ConnectionString(connectionString))
        .uuidRepresentation(UuidRepresentation.STANDARD)
        .serverApi(ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build())
        .build();

    return MongoClients.create(settings);
  }

  private static CodecRegistry buildCodecRegistry() {
    return fromRegistries(
        CodecRegistries.fromCodecs(new LocationCodec()),
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(
            PojoCodecProvider.builder()
                .automatic(true)
                .build()
        )
    );
  }

  private static class LocationCodec implements Codec<Location> {

    @Override
    public Location decode(BsonReader bsonReader, DecoderContext decoderContext) {
      Location location = new Location(null, 0, 0, 0);
      bsonReader.readStartDocument();
      location.setWorld(Bukkit.getWorld(bsonReader.readString("world")));
      location.setX(bsonReader.readDouble("x"));
      location.setY(bsonReader.readDouble("y"));
      location.setZ(bsonReader.readDouble("z"));
      location.setYaw((float) bsonReader.readDouble("yaw"));
      location.setPitch((float) bsonReader.readDouble("pitch"));
      bsonReader.readEndDocument();
      return location;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Location location, EncoderContext encoderContext) {
      bsonWriter.writeStartDocument();
      bsonWriter.writeString("world", location.getWorld().getName());
      bsonWriter.writeDouble("x", location.getX());
      bsonWriter.writeDouble("y", location.getY());
      bsonWriter.writeDouble("z", location.getZ());
      bsonWriter.writeDouble("yaw", location.getYaw());
      bsonWriter.writeDouble("pitch", location.getPitch());
      bsonWriter.writeEndDocument();
    }

    @Override
    public Class<Location> getEncoderClass() {
      return Location.class;
    }
  }

}
