
var MongoClient = require('mongodb').MongoClient;
var url = 'mongodb://root:yuvesh95@ds115931.mlab.com:15931/yuvesh1o';

MongoClient.connect(url, function(err, db) {
    if (err) throw err;
    var dbase = db.db("yuvesh");
    dbase.createCollection("newCollection", function(err, res) {
        if (err) throw err;
        console.log("Collection created!");
        db.close();
    });
});
