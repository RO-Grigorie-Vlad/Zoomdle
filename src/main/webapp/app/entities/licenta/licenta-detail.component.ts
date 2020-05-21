import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILicenta } from 'app/shared/model/licenta.model';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-licenta-detail',
  templateUrl: './licenta-detail.component.html'
})
export class LicentaDetailComponent implements OnInit {
  licenta: ILicenta | null = null;
  isProfesor!: Boolean;
  currentUserLogin!: string;
  curentProfesorID!: number;

  constructor(protected activatedRoute: ActivatedRoute, protected accountService: AccountService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ licenta }) => (this.licenta = licenta));

    this.accountService
      .getAuthenticationState()
      .subscribe(account => (account == null ? (this.currentUserLogin = '') : (this.currentUserLogin = account.login)));

    this.isProfesor = this.accountService.hasAnyAuthority('ROLE_PROFESOR');
    if (this.isProfesor === true) {
      this.accountService.getCurrentProfesorId(this.currentUserLogin).subscribe(response => (this.curentProfesorID = response));
    }
  }

  previousState(): void {
    window.history.back();
  }
}
