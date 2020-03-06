import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILicenta } from 'app/shared/model/licenta.model';

@Component({
  selector: 'jhi-licenta-detail',
  templateUrl: './licenta-detail.component.html'
})
export class LicentaDetailComponent implements OnInit {
  licenta: ILicenta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ licenta }) => (this.licenta = licenta));
  }

  previousState(): void {
    window.history.back();
  }
}
