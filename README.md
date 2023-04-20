# uhg

1. Payment Gateway Integration Adapter - CieloPGAdapterService -
Vendor - Cielo
Route - Checkout via API
Vendor Document - https://developercielo.github.io/en/manual/checkout-cielo#integrating-checkout-cielo

Request :
  An api call would be made while clicking on Pay Now button on Appointment Schedule page. 
Response :
  Response - Transactional screen - It is the Response returned with data to send the buyer to the transactional screen
  Response - Transaction Finished - Contains data on the result of the transaction, after the buyer clicks “Finish” on the transaction screen. It is returned only via Notification


2.The Python Script allows you to translate the JSON documents from source to destination language. 
Imported certain libraries: 
  -Json library - for reading or writing into .JSON files
  -GoogleTrans library - for translation
                        to install the GoogleTrans library the run this command in terminal - pip install googletrans==3.1.0a0 
  -Re library - for checking the regex![diagram drawio](https://user-images.githubusercontent.com/64946238/233289573-b84e3e2a-55ca-4166-bdb0-59e2ce47c9fc.png)
