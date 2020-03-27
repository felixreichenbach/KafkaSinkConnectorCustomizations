package de.demo.kafka;

import org.bson.*;

import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.kafka.connect.sink.converter.SinkDocument;
import com.mongodb.kafka.connect.sink.writemodel.strategy.WriteModelStrategy;

import org.apache.kafka.connect.errors.DataException;

public class CustomWriteModelStrategy implements WriteModelStrategy {

    private static final UpdateOptions UPDATE_OPTIONS = new UpdateOptions().upsert(true);

    //incomming json should have one message key e.g. { "message": "Hello World"}
    @Override
    public WriteModel<BsonDocument> createWriteModel(SinkDocument document) {
        
        // Retrieve the value part of the SinkDocument
        BsonDocument vd = document.getValueDoc().orElseThrow(
                () -> new DataException("Error: cannot build the WriteModel since the value document was missing unexpectedly"));

        // extract message from incoming document
        BsonString message = new BsonString("");
        if (vd.containsKey("message")) {
            message = vd.get("message").asString();
        }

        // Define the filter part of the update statement
        BsonDocument filters = new BsonDocument("counter", new BsonDocument("$lt", new BsonInt32(10)));

        // Define the update part of the update statement
        BsonDocument updateStatement = new BsonDocument();
        updateStatement.append("$inc", new BsonDocument("counter", new BsonInt32(1)));
        updateStatement.append("$push", new BsonDocument("messages", new BsonDocument("message", message)));

        // Return the full update å
        return new UpdateOneModel<BsonDocument>(
                filters,
                updateStatement,
                UPDATE_OPTIONS
        );
    }
}
