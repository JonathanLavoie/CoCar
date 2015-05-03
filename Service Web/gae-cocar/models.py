'''
Created on 2015-04-03

@author: Jimmy et Jonathan
'''
from google.appengine.ext import ndb

class ParcoursConducteur(ndb.Model):
    departC = ndb.StringProperty()
    destinationC = ndb.StringProperty()
    identifiantCree = ndb.StringProperty()
    dateHeureC = ndb.DateTimeProperty()
    nombrePlace = ndb.IntegerProperty()
    nbKm = ndb.IntegerProperty()
    
class ParcoursPassager(ndb.Model):  
    departP = ndb.StringProperty()
    destinationP = ndb.StringProperty()
    identifiantCree = ndb.StringProperty()
    nombrePassager = ndb.IntegerProperty()
    dateHeureP = ndb.DateTimeProperty()
    
class User(ndb.Model):
    userNom = ndb.StringProperty()
    userPW = ndb.StringProperty()
    userAdresse = ndb.StringProperty()
    userPhone = ndb.StringProperty()
    userSumRate = ndb.IntegerProperty()
    userCountRate = ndb.IntegerProperty()

class DepartUser(ndb.Model):
    userId1 = ndb.StringProperty()
    userId2 = ndb.StringProperty()
    parcourId = ndb.StringProperty()
    nbPassager = ndb.IntegerProperty()
    rate = ndb.IntegerProperty()