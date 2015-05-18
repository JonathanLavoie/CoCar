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
from models import ParcoursConducteur,ParcoursPassager,User,DepartUser

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
    
    def get(self,iden = None,lat2 = None,long2 = None):
        try:
            #obtient toutes les conducteurs
            resultat = []
            if (iden is None):
                qr = ParcoursConducteur.query()
                for p in qr:
                    dictConducteur = p.to_dict()
                    latLong = dictConducteur['departC']
                    vectlatLong = latLong.split(';')
                    latLongDest = dictConducteur['destinationC']
                    vectlatLongDest = latLongDest.split(';')
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
                cle = ndb.Key('ParcoursConducteur',iden)
                qr = cle.get()
                dictConducteur = {}
                dictConducteur['id'] = iden
                dictConducteur['dateHeureC'] = qr.dateHeureC
                dictConducteur['departC'] = qr.departC
                dictConducteur['destinationC'] = qr.destinationC
                dictConducteur['identifiantCree'] = qr.identifiantCree
                dictConducteur['nbKm'] = qr.nbKm
                dictConducteur['nombrePlace'] = qr.nombrePlace
                latLong = dictConducteur['departC']
                vectlatLong = latLong.split(';')
                latLongDest = dictConducteur['destinationC']
                vectlatLongDest = latLongDest.split(';')
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
    def put(self,iden = None,nbPlace = None):
        try:   
            if (iden is None):
                iden = idAleatoire()
            cle = ndb.Key('ParcoursConducteur',iden)
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
    def get(self,iden = None,lat2 = None, long2 = None):
        try:
            #obtient toutes les passagers
            resultat = []
            if(iden is None):
                qr = ParcoursPassager.query().order(ParcoursPassager.dateHeureP)

                for p in qr:
                    dictPassager = p.to_dict()
                    latLong = dictPassager['departP']
                    vectlatLong = latLong.split(';')
                    latLongDest = dictPassager['destinationP']
                    vectlatLongDest = latLongDest.split(';')
                    kmDep = calculerDistance(vectlatLong[0], vectlatLong[1], lat2, long2)
                    kmDest = calculerDistance(vectlatLongDest[0], vectlatLongDest[1], lat2, long2)
                    dictPassager['disDest'] = round(kmDest,2)
                    dictPassager['disDep'] = round(kmDep,2)
                    dictPassager['id'] = p.key.id()
                    resultat.append(dictPassager)
                    self.response.headers['Content-Type'] = 'application/json'
            else:
                cle = ndb.Key('ParcoursPassager',iden)
                qr = cle.get();
                dictPassager = {}
                vectlatLongDest = qr.destinationP.split(';')
                vectlatLong = qr.departP.split(';')
                kmDep = calculerDistance(vectlatLong[0], vectlatLong[1], lat2, long2)
                kmDest = calculerDistance(vectlatLongDest[0], vectlatLongDest[1], lat2, long2)
                dictPassager['departP'] = qr.departP
                dictPassager['destinationP'] = qr.destinationP
                dictPassager['identifiantCree'] = qr.identifiantCree
                dictPassager['nombrePassager'] = qr.nombrePassager
                dictPassager['dateHeureP'] = qr.dateHeureP
                dictPassager['disDest'] = round(kmDest,2)
                dictPassager['disDep'] = round(kmDep,2)
                dictPassager['id'] = iden
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
            
            iden = idAleatoire()
        
            cle = ndb.Key('ParcoursPassager',iden)
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
            

class UserHandler(webapp2.RequestHandler):
    def put(self,email):
        cle = ndb.Key('User',email)
        user = cle.get();
        status = 204
        if(user is None):
            user = User(key=cle)
            status = 201
            jsonObj = json.loads(self.request.body)
            user.userNom = jsonObj["nom"]
            user.userPW = jsonObj["PW"]
            user.userAdresse = jsonObj["adresse"]
            user.userPhone = jsonObj["phone"]
            user.userSumRate = int(jsonObj['sumRate'])
            user.userCountRate = int(jsonObj["countRate"])
            user.put()
        self.response.set_status(status)
        
    def get(self,email):
        resultat = []
        cle = ndb.Key('User',email)
        qr = cle.get();
        if(qr is not None):
            dictUser = {}
            dictUser['email'] = email;
            dictUser['nom'] = qr.userNom
            dictUser['PW'] = qr.userPW
            dictUser['adresse'] = qr.userAdresse
            dictUser['phone'] = qr.userPhone
            dictUser['sumRate'] = qr.userSumRate
            dictUser['countRate'] = qr.userCountRate
            resultat.append(dictUser)
        self.response.headers['Content-Type'] = 'application/json'
        self.response.out.write(json.dumps(resultat,default=serialiser_pour_json))

class parcoursUserHandler(webapp2.RequestHandler):
    def put(self):
        try: 
            iden = idAleatoire()
            cle = ndb.Key('DepartUser',iden)
            depart = DepartUser(key=cle)
            jsonObj = json.loads(self.request.body) 
            depart.userId1 = jsonObj["userId1"]
            depart.userId2 = jsonObj["userId2"]
            depart.parcourId = jsonObj["parcourId"]
            depart.nbPassager = int(jsonObj["nbPassager"])
            depart.rate = float(jsonObj["rate"])
            depart.type = jsonObj['type']
            depart.put()
            self.response.set_status(201)      
        except (ValueError, db.BadValueError), ex:
            logging.info(ex)
            self.error(400)
        except Exception, ex:
            logging.info(ex)
            self.error(500)
    
    def post(self,identi,note,user):
        try:
            cle = ndb.Key('DepartUser',identi)
            depart = cle.get()
            depart.rate = float(note) 
            depart.put()
            cle = ndb.Key('User',user)
            user = cle.get()
            user.userSumRate = user.userSumRate + float(note)
            user.userCountRate = user.userCountRate + 1
            user.put()
        except (ValueError, db.BadValueError), ex:
            logging.info(ex)
            self.error(400)
        except Exception, ex:
            logging.info(ex)
            self.error(500)
                
    def get(self,email):
        try:
            qr = DepartUser.query()
            resultat = []
            for p in qr:
                    
                dict = p.to_dict()
                dict["id"] = p.key.id()
                if(dict['userId1'] == email or dict['userId2'] == email):
                    resultat.append(dict)
            self.response.out.write(json.dumps(resultat,default=serialiser_pour_json))    
                
        except (ValueError, db.BadValueError), ex:
            logging.info(ex)
            self.error(400)
        except Exception, ex:
            logging.info(ex)
            self.error(500) 
            
class departPrevuHandler(webapp2.RequestHandler):
    def get(self,iden,type):
        try:
            qr = DepartUser.query()
            resultat = []
            for p in qr:
               dict = p.to_dict()
               if((dict['userId1'] == iden and dict['userId2'] == iden) or (dict['userId1'] == iden or dict['userId2'] == iden)):
                   logging.info(type)
                   if(dict["type"] == type and type == "Passager"):
                       cle = ndb.Key('ParcoursPassager',dict['parcourId'])
                       qr = cle.get();
                       dictPar = {}
                       dictPar['idDep'] = p.key.id()
                       dictPar['id'] = dict['parcourId']
                       dictPar['departP'] = qr.departP
                       dictPar['destinationP'] = qr.destinationP
                       dictPar['identifiantCree'] = qr.identifiantCree
                       dictPar['nombrePassager'] = dict['nbPassager']
                       dictPar['dateHeureP'] = qr.dateHeureP
                       dictPar['disDep'] = "0"
                       dictPar['disDest'] = "0"
                       resultat.append(dictPar) 
                   if(dict["type"] == type and type == "Conducteur"):
                        numero = dict['parcourId']
                        cle = ndb.Key('ParcoursConducteur',numero)
                        qr = cle.get();
                        dictPar = {}
                        dictPar['idDep'] = p.key.id()
                        dictPar['id'] = dict['parcourId']
                        dictPar['departC'] = qr.departC
                        dictPar['destinationC'] = qr.destinationC
                        dictPar['identifiantCree'] = qr.identifiantCree
                        dictPar['nombrePlace'] = dict['nbPassager']
                        dictPar['dateHeureC'] = qr.dateHeureC
                        dictPar['nbKm'] = qr.nbKm
                        dictPar['disDep'] = "0"
                        dictPar['disDest'] = "0"
                        resultat.append(dictPar) 
            self.response.out.write(json.dumps(resultat,default=serialiser_pour_json))
        except (ValueError, db.BadValueError), ex:
            logging.info(ex)
            self.error(400)
        except Exception, ex:
            logging.info(ex)
            self.error(500)
             
class deleteParcours(webapp2.RequestHandler):
    def delete(self,idenPar,idenDep,unType,nbPass):
        try:
            cle = ndb.Key('DepartUser',idenDep)
            cle.delete()
            if(unType == "Conducteur"):
                cle = ndb.Key('ParcoursConducteur',idenPar)
                condu = cle.get()
                condu.nombrePlace = condu.nombrePlace + int(nbPass)
                condu.put()           
        except (ValueError, db.BadValueError), ex:
            logging.info(ex)
            self.error(400)
        except Exception, ex:
            logging.info(ex)
            self.error(500) 
          
application = webapp2.WSGIApplication(
    [
        ('/',                                                                            MainPageHandler),
        webapp2.Route(r'/conducteur/lat/<lat2>/long/<long2>',                            handler=ConducteurHandler, methods=['GET','PUT']),
        webapp2.Route(r'/conducteur/<iden>/lat/<lat2>/long/<long2>',                     handler=ConducteurHandler, methods=['GET']),
        webapp2.Route(r'/passager/<iden>/lat/<lat2>/long/<long2>',                       handler=PassagerHandler, methods=['GET']),                                        
        webapp2.Route(r'/passager/lat/<lat2>/long/<long2>',                              handler=PassagerHandler, methods=['GET','PUT']),
        webapp2.Route(r'/conducteur/<iden>/nbPlace/<nbPlace>',                           handler=ConducteurHandler,methods=['PUT']),
        webapp2.Route(r'/conducteur',                                                    handler=ConducteurHandler,methods=['PUT']),
        webapp2.Route(r'/passager',                                                      handler=PassagerHandler, methods=['PUT']),
        webapp2.Route(r'/depart',                                                        handler=parcoursUserHandler, methods=['PUT']),
		webapp2.Route(r'/depart/<email>',                                                handler=parcoursUserHandler, methods=['GET']),
        webapp2.Route(r'/depart/<identi>/rating/<note>/user/<user>',                     handler=parcoursUserHandler, methods=['POST']),
        webapp2.Route(r'/departPrevu/<iden>/type/<type>',                                handler=departPrevuHandler, methods=['GET']),
        webapp2.Route(r'/user/<email>',                                                  handler=UserHandler, methods=['PUT','GET']),
        webapp2.Route(r'/depart/<idenPar>/idDep/<idenDep>/type/<unType>/nbPlace/<nbPass>', handler=deleteParcours, methods=['DELETE']),
    ],
    debug=True)