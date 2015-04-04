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

from models import ParcoursConducteur,ParcoursPassager

def serialiser_pour_json(obj):
    ''' Retourne une fonction pour mettre un objet en JSON'''

    if (isinstance(obj, datetime.date) or isinstance(obj, datetime.datetime)):
        # Pour une date, on retourne la String du format ISO
        return obj.isoformat()
    else:
        return obj

class MainPageHandler(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
        self.response.out.write('<html><body><h1>Cocar Google App Engine fonctionne bien !</h1></body></html>')

class ConducteurHandler(webapp2.RequestHandler):
    def get(self):
        try:
            #obtient toutes les conducteurs
            resultat = []
            query = ParcoursConducteur.query();
            #filtre pour recupere seulement 
            #logging.info()
            query = query.filter(ParcoursConducteur.dateHeureC >= datetime.datetime.now())
            #logging.info(datetime.datetime.now())


            for p in query:
                dictConducteur = p.to_dict()
                dictConducteur['id'] = p.key.id()
                resultat.append(dictConducteur)
            self.response.headers['Content-Type'] = 'application/json'
            self.response.out.write(json.dumps(resultat,default=serialiser_pour_json))

        except (ValueError, db.BadValueError), ex:
            logging.info(ex)
            self.error(400)
        except Exception, ex:
            logging.info(ex)
            self.error(500)

    def put(self, id):
        try:

            cle = ndb.Key('ParcoursConducteur',id)
            cond = cle.get()
            status = 204

            if (cond is None):
                #nouveauConducteur
                cond = ParcoursConducteur(key=cle)
                status = 201

            jsonObj = json.loads(self.request.body)
            logging.info("ici: " + jsonObj['dateHeureC'])
            #Ajout des champs du nouveau conducteur
            cond.departC = jsonObj['departC']
            cond.destinationC = jsonObj['destinationC']
            cond.dateHeureC = datetime.datetime.strptime(jsonObj['dateHeureC'],'%Y-%m-%d %H:%M')
            cond.nombrePlace = int(jsonObj['nombrePlace'])
            cond.nbKm = int(jsonObj['nbKm'])
            cond.put()
            self.response.set_status(status)

        except (ValueError, db.BadValueError), ex:
            logging.info(ex)
            self.error(400)
        except Exception, ex:
            logging.info(ex)
            self.error(500)

application = webapp2.WSGIApplication(
    [
        ('/',                                               MainPageHandler),
        webapp2.Route(r'/conducteur',                       handler=ConducteurHandler, methods=['GET']),
        webapp2.Route(r'/conducteur/<id>',                  handler=ConducteurHandler, methods=['PUT']),
    ],
    debug=True)
