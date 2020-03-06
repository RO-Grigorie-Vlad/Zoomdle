import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAplicareConsultatie } from 'app/shared/model/aplicare-consultatie.model';

@Component({
  selector: 'jhi-aplicare-consultatie-detail',
  templateUrl: './aplicare-consultatie-detail.component.html'
})
export class AplicareConsultatieDetailComponent implements OnInit {
  aplicareConsultatie: IAplicareConsultatie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aplicareConsultatie }) => (this.aplicareConsultatie = aplicareConsultatie));
  }

  previousState(): void {
    window.history.back();
  }
}
