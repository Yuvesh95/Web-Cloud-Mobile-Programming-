

var MongoClient = require('mongodb').MongoClient;
var url = 'mongodb://root:yuvesh95@ds115931.mlab.com:15931/yuvesh1';

MongoClient.connect(url, function(err, db) {
    if (err) throw err;
    var dbase = db.db("yuvesh1");
    dbase.dropCollection("newCollection", function(err, delOK) {
        if (err) throw err;
        if (delOK) console.log("Collection deleted");
        db.close();
    });
});