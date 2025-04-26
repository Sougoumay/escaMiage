import {Enigme} from './enigme.modele';


export interface Reponse {
  id: number;
  partieId: number;
  question: string;
  difficulte: string;
  indice: string;
  nombreTentative: number;
  score: number;
}

export class Reponse {
  id: number;
  partieId: number;
  question: string;
  difficulte: string; // Utilisation de l'énumération Difficulte
  indice: string;
  nombreTentative: number;
  score: number;
  isRepondu: boolean;

  constructor(
    id: number,
    partieId: number,
    question: string,
    difficulte: string,
    indice: string,
    nombreTentative: number,
    score: number, 
    isRepondu: boolean
  ) {
    this.id = id;
    this.partieId = partieId;
    this.question = question;
    this.difficulte = difficulte;
    this.indice = indice;
    this.nombreTentative = nombreTentative;
    this.score = score;
    this.isRepondu = isRepondu;
  }

 


}



