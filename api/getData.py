import firebase_admin
from firebase_admin import credentials, db

cred = credentials.Certificate("tcnk-e50ef-firebase-adminsdk-ydhen-a568e6740f.json")
firebase_admin.initialize_app(cred,{
    'databaseURL' : "https://tcnk-e50ef-default-rtdb.firebaseio.com",    
    }    
)

class FirebaseConnect:
    def __init__(self, key):
        self.key=key
           
    def getHistory(self):
        dict = db.reference('history').child(self.key).get()
        return [(i['rcp_id'],i['rate']) for i in dict.values()]
    
    def getAllergying(self):
        allergy = db.reference('allergy').child(self.key).get()
        return [i['allergy'] for i in allergy.values()]
    
    def getPrefer(self):
        prefer = db.reference('preference').child(self.key).get()
        return [i['preference'] for i in prefer.values()]

#fc = FirebaseConnect("1XzkdnzZGaeSdt2KBXPiKNoftnA2")
#print(fc.getPrefer())