
import {Difficulte} from './difficulte.enum';


export interface Enigme {
  id?: number;
  question: string;
  reponse: string;
  difficulte: Difficulte;
  indice: string;
  theme: string;
}
