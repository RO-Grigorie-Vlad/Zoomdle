import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LicentaSharedModule } from 'app/shared/shared.module';
import { LicentaComponent } from './licenta.component';
import { LicentaDetailComponent } from './licenta-detail.component';
import { LicentaUpdateComponent } from './licenta-update.component';
import { LicentaDeleteDialogComponent } from './licenta-delete-dialog.component';
import { licentaRoute } from './licenta.route';

@NgModule({
  imports: [LicentaSharedModule, RouterModule.forChild(licentaRoute)],
  declarations: [LicentaComponent, LicentaDetailComponent, LicentaUpdateComponent, LicentaDeleteDialogComponent],
  entryComponents: [LicentaDeleteDialogComponent]
})
export class LicentaLicentaModule {}
