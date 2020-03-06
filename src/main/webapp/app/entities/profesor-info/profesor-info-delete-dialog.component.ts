import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProfesorInfo } from 'app/shared/model/profesor-info.model';
import { ProfesorInfoService } from './profesor-info.service';

@Component({
  templateUrl: './profesor-info-delete-dialog.component.html'
})
export class ProfesorInfoDeleteDialogComponent {
  profesorInfo?: IProfesorInfo;

  constructor(
    protected profesorInfoService: ProfesorInfoService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.profesorInfoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('profesorInfoListModification');
      this.activeModal.close();
    });
  }
}
