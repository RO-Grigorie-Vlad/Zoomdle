import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConsultatie } from 'app/shared/model/consultatie.model';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-consultatie-detail',
  templateUrl: './consultatie-detail.component.html'
})
export class ConsultatieDetailComponent implements OnInit {
  consultatie: IConsultatie | null = null;

  currentUserLogin!: string;
  isProfesor!: Boolean;
  curentProfesorID!: number;

  constructor(protected activatedRoute: ActivatedRoute, protected accountService: AccountService) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .subscribe(account => (account == null ? (this.currentUserLogin = '') : (this.currentUserLogin = account.login)));

    this.isProfesor = this.accountService.hasAnyAuthority('ROLE_PROFESOR');
    if (this.isProfesor === true) {
      this.accountService.getCurrentProfesorId(this.currentUserLogin).subscribe(response => (this.curentProfesorID = response));
    }

    this.activatedRoute.data.subscribe(({ consultatie }) => (this.consultatie = consultatie));
  }

  previousState(): void {
    window.history.back();
  }
}
