
var http = require('http');
var MongoClient = require('mongodb').MongoClient;
var url = 'mongodb://root:yuvesh95@ds115931.mlab.com:15931/yuvesh1';

MongoClient.connect(url, function(err, db) {
    if (err) throw err;
    var dbase = db.db("yuvesh");
    var myquery = { address: 'Main Road 989' };
    dbase.collection("newCollection").deleteOne(myquery, function(err, obj) {
        if (err) throw err;
        console.log(obj.result.n + " document(s) deleted");
        db.close();
    });
});