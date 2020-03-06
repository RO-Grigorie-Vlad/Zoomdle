import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LicentaSharedModule } from 'app/shared/shared.module';
import { ProfesorInfoComponent } from './profesor-info.component';
import { ProfesorInfoDetailComponent } from './profesor-info-detail.component';
import { ProfesorInfoUpdateComponent } from './profesor-info-update.component';
import { ProfesorInfoDeleteDialogComponent } from './profesor-info-delete-dialog.component';
import { profesorInfoRoute } from './profesor-info.route';

@NgModule({
  imports: [LicentaSharedModule, RouterModule.forChild(profesorInfoRoute)],
  declarations: [ProfesorInfoComponent, ProfesorInfoDetailComponent, ProfesorInfoUpdateComponent, ProfesorInfoDeleteDialogComponent],
  entryComponents: [ProfesorInfoDeleteDialogComponent]
})
export class LicentaProfesorInfoModule {}
