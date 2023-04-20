var cursor = db.user.find({})

function reverseString(str) {
    return str.split("").reverse().join("");
}

function anonymizeEmail(email) {
var EmailAddress = email;
    var SPLIT = "@";
    var string_email = email.toString().toLowerCase();
    var Email_Split = string_email.split("@");
	if(Email_Split[1] != "mphrx.com" || Email_Split[1] != "test.com" || Email_Split[1] != "abc.com"){
	var emailFirstPart = reverseString(Email_Split[0]);
    EmailAddress = emailFirstPart.concat(SPLIT, "testrandom.com")
	}
    return EmailAddress;
}

function getNumberBasedOnCountryCode(countryCode) {
    var finalNumber;
    if (countryCode == "+1" || countryCode == "1") {
        finalNumber = "2025550106";
    }
    if (countryCode == "91" || countryCode == "+91") {
        finalNumber = "7555180274";
    }
    if (countryCode == "BRA") {
        finalNumber = "9557079118";
    }
	if (countryCode == "55" || countryCode == "+55"){
	        finalNumber = "9550046194";
	}
    return finalNumber;
}
var count=0;
while (cursor.hasNext()) {
    var item = cursor.next();
    if (item.countryCode) {
        item.phoneNo = getNumberBasedOnCountryCode(item.countryCode);
    }
    if (item.email) {
        item.email = anonymizeEmail(item.email);
    }
  
    item.reversed1 = true
        db.user.save(item)
        print("Updated : " + item._id)
		count++;
}

print("total updated record : "+count)