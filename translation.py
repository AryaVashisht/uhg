import os
import re
import json
from googletrans import Translator
import logging as log

print("Enter the source and destination language codes as per the supported languages list.")
print("Source Language: ")
from_lang = input()
print("Destination Language: ")
to_lang = input()

print("Enter the path which contains the files that are to be translated: ")
path = input()

current_dir = os.getcwd()
print("Current : "+current_dir)

regex = re.compile('[@$^*~<>}{]')
translator = Translator()

log.basicConfig(filename='translationModule.log', level=log.INFO)

print("It's executing.")
log.info("Script execution started.")
print("The values which are getting translated.\n...")
log.info("Printed would be the values which are not translated.\n...")

#############################  File Reading and Writing  ##################################
# READS the JSON file
def fileReader(file_path):
    print("fileR " + file_path)
    f = open(file_path, "rb")
    x = json.loads(f.read())
    print(x)
    return x

# Writes to the JSON file after translation
def fileWriter(x, file_name, current_dir):
    print("fileW\n")
    os.chdir(current_dir)
    output_folder = "output_JSON"

    if os.path.isdir(output_folder):
        print("Folder Already Exists")
    else:
        os.mkdir(output_folder)

    json_object = json.dumps(x, indent=4, ensure_ascii=False).encode('utf-8')
    with open(f"{output_folder}\{file_name.split('.json')[0]}_translated.json", "wb") as outfile:
        outfile.write(json_object)


################################   Translation of the values   #####################################
def isDictOrString(x):
    """To check weather the value is dictionary or not"""
    if type(x) is str:
        return False
    else:
        return True

def translateString(v, from_lang, to_lang):
    """To translate the value if the value is string"""
    if ((regex.search(v) == None) and v!="" and v!=None ):
        print(v)
        translated = translator.translate(v, dest=to_lang, src=from_lang)
        return translated.text
    else:
        if(v!=""):
            log.info(v)
        return v

def translateDict(x, from_lang, to_lang):
    """To translate the complete dictionary at once"""
    for item in x.items():
        k, v = item
        if isDictOrString(v):
            x[k] = translateDict(v, from_lang, to_lang)
        else:
            x[k] = translateString(v, from_lang, to_lang)
    return x

##############################   Execution starts here   ################################
if os.path.isdir(path):
    os.chdir(path)
    for file in os.listdir():
        # Check whether file is in json format or not
        if file.endswith(".json"):
            file_path = f"{path}\{file}"
            print("\t"+file_path)
            x = fileReader(file_path)
            log.info("\n\nNon Translated values for "+ file)
            translateDict(x, from_lang, to_lang)
            fileWriter(x, file, current_dir)

else:
    print("Directory Not Found")
print("Executed Completely")
log.info("Execution Completed successfully")