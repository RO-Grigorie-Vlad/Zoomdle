import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAplicareLicenta } from 'app/shared/model/aplicare-licenta.model';

@Component({
  selector: 'jhi-aplicare-licenta-detail',
  templateUrl: './aplicare-licenta-detail.component.html'
})
export class AplicareLicentaDetailComponent implements OnInit {
  aplicareLicenta: IAplicareLicenta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aplicareLicenta }) => (this.aplicareLicenta = aplicareLicenta));
  }

  previousState(): void {
    window.history.back();
  }
}
