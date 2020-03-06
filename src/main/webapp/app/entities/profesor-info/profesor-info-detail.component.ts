import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProfesorInfo } from 'app/shared/model/profesor-info.model';

@Component({
  selector: 'jhi-profesor-info-detail',
  templateUrl: './profesor-info-detail.component.html'
})
export class ProfesorInfoDetailComponent implements OnInit {
  profesorInfo: IProfesorInfo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profesorInfo }) => (this.profesorInfo = profesorInfo));
  }

  previousState(): void {
    window.history.back();
  }
}
