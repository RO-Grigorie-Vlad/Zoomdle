export class AplicareDTO {
  idLicentaOrConsulatie: number;
  currentUserLogin: string;
  constructor(currentUserLogin: string, idLicentaOrConsulatie: number) {
    this.idLicentaOrConsulatie = idLicentaOrConsulatie;
    this.currentUserLogin = currentUserLogin;
  }
}
