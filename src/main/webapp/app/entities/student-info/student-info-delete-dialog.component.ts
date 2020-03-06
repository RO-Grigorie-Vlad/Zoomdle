import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStudentInfo } from 'app/shared/model/student-info.model';
import { StudentInfoService } from './student-info.service';

@Component({
  templateUrl: './student-info-delete-dialog.component.html'
})
export class StudentInfoDeleteDialogComponent {
  studentInfo?: IStudentInfo;

  constructor(
    protected studentInfoService: StudentInfoService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentInfoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('studentInfoListModification');
      this.activeModal.close();
    });
  }
}
