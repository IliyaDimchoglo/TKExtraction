# TKExtraction

login - (POST)/acess_token {

(Basic: username - client, password - secret),

(Query: grant_type - password
        username - %username
        password - %password);
}

submit - (POST)/cvs/submit (request param - file(pdf), access_token);

retieve - (GET)/cvs/retieve (request param - processId(long), acess_token);
