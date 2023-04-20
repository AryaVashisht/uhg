var cursor = db.patient.find({})

function reverseString(str) {
    return str.split("").reverse().join("");
}

function reverseEmail(email) {
var EmailAddress = email;
    var SPLIT = "@";
    var string_email = email.toString().toLowerCase();
    var Email_Split = string_email.split("@");
	if(Email_Split[1] != "mphrx.com"){
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
	try{
    if ((typeof item.telecom != 'undefined') && (item.telecom.length > 0) && (item.telecom != null)) {
        for (k = 0; k < item.telecom.length; k++) {
			if(item.telecom[k].system!=null){
            if ((item.telecom[k].system.value == "phone") && (typeof item.telecom[k].value != 'undefined') && (item.telecom[k].system.value != null)) {
                var countryCode = item.telecom[k].countryCode.value;
                var phone = getNumberBasedOnCountryCode(countryCode);
                item.telecom[k].modifiedValue.value=phone;
                item.telecom[k].value.value=phone;
                print("Updating Phone")
            }
            if((item.telecom[k].system.value == "email") && (typeof item.telecom[k].value != 'undefined')&& (item.telecom[k].system.value != null)) {
                var emailAdd = item.telecom[k].modifiedValue.value;
                item.telecom[k].modifiedValue.value=reverseEmail(emailAdd);
                item.telecom[k].value.value=reverseEmail(emailAdd);
                print("Updating Email")
                count++;
            }
			}
        }
    }
	}
	catch(e){
		print("no telecom found")
	}
    item.reversed1=true
    db.patient.save(item)
    print(" Updated: "+item._id)
}
print("total updated entries : "+count);