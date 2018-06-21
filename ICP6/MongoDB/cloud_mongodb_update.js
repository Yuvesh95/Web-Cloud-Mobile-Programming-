
var MongoClient = require('mongodb').MongoClient;
var url = 'mongodb://root:yuvesh95@ds115931.mlab.com:15931/yuvesh1';

MongoClient.connect(url, function(err, db) {
    if (err) throw err;
    var dbase = db.db("yuvesh");
    var myquery = { address: /^S/ };
    var newvalues = {$set: {name: "Minnie"} };
    var myoptions = { multi: true };
    dbase.collection("newCollection").updateMany(myquery, newvalues, myoptions, function(err, res) {
        if (err) throw err;
        console.log(res.result.nModified + " record(s) updated");
        db.close();
    });
});
