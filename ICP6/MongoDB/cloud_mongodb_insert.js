var MongoClient = require('mongodb').MongoClient;
var assert = require('assert');
var url = 'mongodb://root:yuvesh95@ds115931.mlab.com:15931/yuvesh1';

var insertDocument = function(db, callback) {
    db.collection('yuvesh').insertOne( {
        "fname" : "yuvesh",
        "lname" : "sai",
        "address":{
            "city":"Kansas City",
            "state":"MO"
        },
        "education" : {
            "university":"UMKC",
            "degree":"Master of Science",
            "major":"Computer Science"
        },
        "mail":"lkbqc@mail.umkc.edu"
    }, function(err, result) {
        assert.equal(err, null);
        console.log("Inserted a document into the yuvesh collection.");
        callback();
    });
};
MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);
    insertDocument(db, function() {
        db.close();
    });
});