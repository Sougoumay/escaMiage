import {ReactionUtilisateurDTO} from './reaction-utilisateur.modele';


export interface Post {
  idPost?: number;
  contenu: string;
  utilisateurDTO?: number;
  imagePost?: any;
  reactionDTOList?: ReactionUtilisateurDTO[]
}
