/* after changing this file run 'npm run webpack:build' */
import '../content/scss/vendor.scss';
import { library } from '@fortawesome/fontawesome-svg-core';
import {
  // other imports
  faFileSignature,
  faLock,
  faBook,
  faCalendar,
  faClock,
  faCheck
} from '@fortawesome/free-solid-svg-icons';
library.add(faCalendar);
library.add(faBook);
library.add(faLock);
library.add(faFileSignature);
library.add(faClock);
library.add(faCheck);
