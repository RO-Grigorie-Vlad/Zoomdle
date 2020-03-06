import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAplicareConsultatie } from 'app/shared/model/aplicare-consultatie.model';
import { AplicareConsultatieService } from './aplicare-consultatie.service';

@Component({
  templateUrl: './aplicare-consultatie-delete-dialog.component.html'
})
export class AplicareConsultatieDeleteDialogComponent {
  aplicareConsultatie?: IAplicareConsultatie;

  constructor(
    protected aplicareConsultatieService: AplicareConsultatieService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aplicareConsultatieService.delete(id).subscribe(() => {
      this.eventManager.broadcast('aplicareConsultatieListModification');
      this.activeModal.close();
    });
  }
}
