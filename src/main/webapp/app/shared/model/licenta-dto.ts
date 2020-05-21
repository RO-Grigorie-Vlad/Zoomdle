export class LicentaDTO {
  licentaID: number;
  currentUserLogin: string;
  constructor(currentUserLogin: string, licentaID: number) {
    this.licentaID = licentaID;
    this.currentUserLogin = currentUserLogin;
  }
}
