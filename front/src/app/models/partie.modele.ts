import {Reponse} from './reponse.modele';

export interface Partie {
  id: number;
  idUtilisateur: number;
  etat: Etat;
  scoreFinal: number;
  dateCreation: Date;
  tempsAlloue: number;
  tempsFinal: Date;
  reponses: Reponse[];

}
