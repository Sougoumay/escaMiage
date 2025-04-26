
import {Post} from './post.modele';
import {UserActu} from './userActu.modele';

export interface ReactionUtilisateurDTO {

  typeReaction: string;
  idUtilisateur?: number | null;
  postDTO?: Post;
}
