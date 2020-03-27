package de.demo.kafka;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.*;
import org.bson.conversions.Bson;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.kafka.connect.sink.converter.SinkDocument;
import com.mongodb.kafka.connect.sink.writemodel.strategy.WriteModelStrategy;
import static com.mongodb.kafka.connect.sink.MongoSinkTopicConfig.ID_FIELD;

import org.apache.kafka.connect.errors.DataException;


public class CustomWriteModelStrategy implements WriteModelStrategy {

    private static final UpdateOptions UPDATE_OPTIONS = new UpdateOptions().upsert(true);

    @Override
    public WriteModel<BsonDocument> createWriteModel(SinkDocument document) {
        
        // Retrieve the value part of the SinkDocument
        BsonDocument vd = document.getValueDoc().orElseThrow(
                () -> new DataException("Error: cannot build the WriteModel since the value document was missing unexpectedly"));
        
        // Retrieve a specific the value of field "counter" and increment
        int counter = vd.get("counter").asInt32().intValue();
        counter = counter + 1;

        if (counter == 0) {
            counter = 100;
        }

        // Define the filter part of the update statement
        BsonDocument filters = new BsonDocument("counter", new BsonDocument("$gte", new BsonInt32(0)));

        // Define the update part of the update statement
        BsonDocument updateStatement = new BsonDocument();
        updateStatement.append("$set", new BsonDocument("counter", new BsonInt32(counter)));
        updateStatement.append("$set", new BsonDocument("MessageFrom", new BsonString("CustomWriteStrategy" + counter)));

        // Return the full update statement
        return new UpdateOneModel<BsonDocument>(
                filters,
                updateStatement,
                UPDATE_OPTIONS
        );
    }
}
