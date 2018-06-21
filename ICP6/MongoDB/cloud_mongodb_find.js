

var MongoClient = require('mongodb').MongoClient;
var url = 'mongodb://root:yuvesh95@ds115931.mlab.com:15931/yuvesh1';

MongoClient.connect(url, function(err, db) {
    if (err) throw err;
    var dbase = db.db("yuvesh");
    dbase.collection("newCollection").find({}).toArray(function(err, result) {
        if (err) throw err;
        console.log(result);
        db.close();
    });
});
