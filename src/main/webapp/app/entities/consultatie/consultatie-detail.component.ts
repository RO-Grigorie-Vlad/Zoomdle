import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConsultatie } from 'app/shared/model/consultatie.model';

@Component({
  selector: 'jhi-consultatie-detail',
  templateUrl: './consultatie-detail.component.html'
})
export class ConsultatieDetailComponent implements OnInit {
  consultatie: IConsultatie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consultatie }) => (this.consultatie = consultatie));
  }

  previousState(): void {
    window.history.back();
  }
}
