'''
Created on 2015-04-03

@author: Jimmy et Jonathan
'''
from google.appengine.ext import ndb

class ParcoursConducteur(ndb.Model):
    departC = ndb.StringProperty()
    destinationC = ndb.StringProperty()
    dateHeureC = ndb.DateTimeProperty()
    nombrePlace = ndb.IntegerProperty()
    nbKm = ndb.IntegerProperty()
    
class ParcoursPassager(ndb.Model):
    
    departP = ndb.StringProperty()
    destinationP = ndb.StringProperty()
    nombrePassager = ndb.IntegerProperty()
    dateP = ndb.StringProperty()
    heureP = ndb.StringProperty()