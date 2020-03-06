import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LicentaSharedModule } from 'app/shared/shared.module';
import { StudentInfoComponent } from './student-info.component';
import { StudentInfoDetailComponent } from './student-info-detail.component';
import { StudentInfoUpdateComponent } from './student-info-update.component';
import { StudentInfoDeleteDialogComponent } from './student-info-delete-dialog.component';
import { studentInfoRoute } from './student-info.route';

@NgModule({
  imports: [LicentaSharedModule, RouterModule.forChild(studentInfoRoute)],
  declarations: [StudentInfoComponent, StudentInfoDetailComponent, StudentInfoUpdateComponent, StudentInfoDeleteDialogComponent],
  entryComponents: [StudentInfoDeleteDialogComponent]
})
export class LicentaStudentInfoModule {}
