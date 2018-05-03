// HTTP GET PDF from Signicat SDS
// using Node.js (http://nodejs.org)
// and the request library (https://github.com/mikeal/request)

var fs = require('fs');
var request = require('request');

var url = 'https://preprod.signicat.com/doc/demo/sds/23082013f7mycvs9u35wrwg954qa9wmsn50mtwlatf3ygjy09yj8kkp3q';
request(url).auth('demo', 'Bond007').pipe(fs.createWriteStream('mydownloadedfile.pdf'));
