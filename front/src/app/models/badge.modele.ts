export class Badge {
  id?: number;
  nom: string;
  icone: string;
  condition_type: string;
  condition_value: string;

  constructor(
    nom: string = '',
    icone: string = '',
    condition_type: string = '',
    condition_value: string = '',
    id?: number
  ) {
    this.id = id;
    this.nom = nom;
    this.icone = icone;
    this.condition_type = condition_type;
    this.condition_value = condition_value;
  }

  // Méthode statique pour obtenir un Badge vide
  static empty(): Badge {
    return new Badge();
  }
}
