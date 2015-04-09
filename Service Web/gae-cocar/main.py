'''
Created on 2015-04-03

@author: Jimmy
'''

import webapp2
import logging
import json
import datetime

from google.appengine.ext import ndb
from google.appengine.ext import db
from random import randint
from math import radians,cos,sin,asin,sqrt
from models import ParcoursConducteur,ParcoursPassager

def serialiser_pour_json(obj):
    ''' Retourne une fonction pour mettre un objet en JSON'''

    if (isinstance(obj, datetime.date) or isinstance(obj, datetime.datetime)):
        # Pour une date, on retourne la String du format ISO
        return obj.isoformat()
    else:
        return obj


def idAleatoire():
    char = ["1","2","3","4","5","6","7","8","9","0",
            "a","b","c","d","e","f","g","h","i","j",
            "k","l","m","n","o","p","q","r","s","t",
            "u","v","w","x","y","z"]
    chaine = ""
    for x in range(0,7):
        index = randint(0,35)
        chaine += char[index]
    return chaine;


def calculerDistance(lat1,long1,lat2,long2):
    lat1,long1,lat2,long2 = map(radians,[float(lat1),float(long1),float(lat2),float(long2)])
    dlong = long2 - long1
    dLat = lat2 - lat1
    a = sin(dLat/2)**2 + cos(lat1) * cos(lat2) * sin(dlong/2)**2
    c = 2 * asin(sqrt(a))
    km = 6367 * c 
    return km

class MainPageHandler(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
        self.response.out.write('<html><body><h1>Cocar Google App Engine fonctionne bien !</h1></body></html>')

class ConducteurHandler(webapp2.RequestHandler):
    
    def get(self,id = None,lat2 = None,long2 = None):
        try:
            #obtient toutes les conducteurs
            
            resultat = []
            if (id is None):
                qr = ParcoursConducteur.query()
                for p in qr:
                    dictConducteur = p.to_dict()
                    latLong = dictConducteur['departC']
                    vectlatLong = latLong.split(',')
                    latLongDest = dictConducteur['destinationC']
                    vectlatLongDest = latLongDest.split(',')
                    kmDep = calculerDistance(vectlatLong[0], vectlatLong[1], lat2, long2)
                    kmDest = calculerDistance(vectlatLongDest[0], vectlatLongDest[1], lat2, long2)
                    dictConducteur['id'] = p.key.id()
                    dictConducteur['disDest'] = round(kmDest,2)
                    dictConducteur['disDep'] = round(kmDep,2)
                    km = kmDep + kmDest
                    if (km <= float(dictConducteur['nbKm'])):
                        if (int(dictConducteur['nombrePlace']) > 0):
                            resultat.append(dictConducteur)
                            self.response.headers['Content-Type'] = 'application/json'
            else:
                cle = ndb.Key('ParcoursConducteur',id)
                qr = cle.get()
                dictConducteur = {}
                dictConducteur['id'] = id
                dictConducteur['dateHeureC'] = qr.dateHeureC
                dictConducteur['departC'] = qr.departC
                dictConducteur['destinationC'] = qr.destinationC
                dictConducteur['identifiantCree'] = qr.identifiantCree
                dictConducteur['nbKm'] = qr.nbKm
                dictConducteur['nombrePlace'] = qr.nombrePlace
                latLong = dictConducteur['departC']
                vectlatLong = latLong.split(',')
                latLongDest = dictConducteur['destinationC']
                vectlatLongDest = latLongDest.split(',')
                kmDep = calculerDistance(vectlatLong[0], vectlatLong[1], lat2, long2)
                kmDest = calculerDistance(vectlatLongDest[0], vectlatLongDest[1], lat2, long2)
                dictConducteur['disDest'] = round(kmDest,2)
                dictConducteur['disDep'] = round(kmDep,2)
                resultat.append(dictConducteur)
            
            self.response.out.write(json.dumps(resultat,default=serialiser_pour_json))

        except (ValueError, db.BadValueError), ex:
            logging.info(ex)
            self.error(400)
        except Exception, ex:
            logging.info(ex)
            self.error(500)

    #Permet d'inserer un nouveau conducteur
    def put(self,id = None,nbPlace = None):
        try:   
            if (id is None):
                id = idAleatoire()
            cle = ndb.Key('ParcoursConducteur',id)
            cond = cle.get()
            status = 204
            if (cond is None):
                #nouveauConducteur
                cond = ParcoursConducteur(key=cle)
                status = 201
                
                jsonObj = json.loads(self.request.body)
                #Ajout des champs du nouveau conducteur
                cond.departC = jsonObj['departC']
                cond.destinationC = jsonObj['destinationC']
                cond.dateHeureC = datetime.datetime.strptime(jsonObj['dateHeureC'],'%Y-%m-%d %H:%M')
                cond.identifiantCree = jsonObj['identifiantCree']
                cond.nombrePlace = int(jsonObj['nombrePlace'])
                cond.nbKm = int(jsonObj['nbKm'])
                cond.put()
                
            else:
                logging.info(nbPlace)
                if (nbPlace is not None):
                    if(cond.nombrePlace >= int(nbPlace)):
                        logging.info(nbPlace)
                        cond.nombrePlace = cond.nombrePlace - int(nbPlace)
                        status = 200
                        cond.put()
                    else:   
                        status = 204
                else: 
                    status = 204       
            self.response.set_status(status)

        except (ValueError, db.BadValueError), ex:
            logging.info(ex)
            self.error(400)
        except Exception, ex:
            logging.info(ex)
            self.error(500)
             
class PassagerHandler(webapp2.RequestHandler):
    def get(self,lat2 = None, long2 = None):
        try:
            #obtient toutes les passagers
            resultat = []
            qr = ParcoursPassager.query().order(ParcoursPassager.dateHeureP)

            for p in qr:
                dictPassager = p.to_dict()
                latLong = dictPassager['departP']
                vectlatLong = latLong.split(',')
                latLongDest = dictPassager['destinationP']
                vectlatLongDest = latLongDest.split(',')
                kmDep = calculerDistance(vectlatLong[0], vectlatLong[1], lat2, long2)
                kmDest = calculerDistance(vectlatLongDest[0], vectlatLongDest[1], lat2, long2)
                dictPassager['disDest'] = round(kmDest,2)
                dictPassager['disDep'] = round(kmDep,2)
                dictPassager['id'] = p.key.id()             
                resultat.append(dictPassager)
                self.response.headers['Content-Type'] = 'application/json'
            self.response.out.write(json.dumps(resultat,default=serialiser_pour_json))

        except (ValueError, db.BadValueError), ex:
            logging.info(ex)
            self.error(400)
        except Exception, ex:
            logging.info(ex)
            self.error(500)
    
    #Permet de rajoute un nouveau passager
    def put(self):
        try:
            
            id = idAleatoire()
        
            cle = ndb.Key('ParcoursPassager',id)
            passa = cle.get()
            status = 204

            if (passa is None):
                #nouveauConducteur
                passa = ParcoursPassager(key=cle)
                status = 201
                
                jsonObj = json.loads(self.request.body)
                
                #Ajout des champs du nouveau conducteur
                passa.departP = jsonObj['departP']
                passa.destinationP = jsonObj['destinationP']
                passa.dateHeureP = datetime.datetime.strptime(jsonObj['dateHeureP'],'%Y-%m-%d %H:%M')
                passa.identifiantCree = jsonObj['identifiantCree']
                passa.nombrePassager = int(jsonObj['nombrePassager'])
                logging.info(passa.dateHeureP)
                passa.put()
            self.response.set_status(status)

        except (ValueError, db.BadValueError), ex:
            logging.info(ex)
            self.error(400)
        except Exception, ex:
            logging.info(ex)
            self.error(500)
            
            
application = webapp2.WSGIApplication(
    [
        ('/',                                                       MainPageHandler),
        webapp2.Route(r'/conducteur/lat/<lat2>/long/<long2>',       handler=ConducteurHandler, methods=['GET','PUT']),
        webapp2.Route(r'/conducteur/<id>/lat/<lat2>/long/<long2>',  handler=ConducteurHandler, methods=['GET']),                                               
        webapp2.Route(r'/passager/lat/<lat2>/long/<long2>',         handler=PassagerHandler, methods=['GET','PUT']),
        webapp2.Route(r'/conducteur/<id>/nbPlace/<nbPlace>',        handler=ConducteurHandler,methods=['PUT']),
        webapp2.Route(r'/conducteur',                               handler=ConducteurHandler,methods=['PUT']),
        webapp2.Route(r'/passager',                                 handler=PassagerHandler, methods=['PUT']),
    ],
    debug=True)