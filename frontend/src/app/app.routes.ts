import {Routes} from '@angular/router';

import {Role} from './enum/role';
import {Admin} from './page/admin/admin';
import {ArticleEdit} from './page/article-edit/article-edit';
import {ArticleNew} from './page/article-new/article-new';
import {ArticleReview} from './page/article-review/article-review';
import {ArticleShow} from './page/article-show/article-show';
import {MainPage} from './page/main-page/main-page';
import {Register} from './page/register/register';
import {ReviewerBoard} from './page/reviewer-board/reviewer-board';
import {Signin} from './page/signin/signin';

export const routesDict = [
  {
    name: 'Home',
    route: {path: '', component: MainPage},
    roles: [Role.GUEST, Role.USER, Role.ADMINISTRATOR, Role.MODERATOR]
  },
  {
    name: 'SignIn',
    route: {path: 'signin', component: Signin},
    roles: [Role.GUEST]
  },
  {
    name: 'Register',
    route: {path: 'register', component: Register},
    roles: [Role.GUEST]
  },
  {
    name: 'New',
    route: {path: 'new', component: ArticleNew},
    roles: [Role.USER, Role.ADMINISTRATOR, Role.MODERATOR]
  },
  {
    name: 'Admin',
    route: {path: 'admin', component: Admin},
    roles: [Role.ADMINISTRATOR]
  },
  {
    name: 'Review',
    route: {path: 'reviewer', component: ReviewerBoard},
    roles: [Role.ADMINISTRATOR, Role.MODERATOR]
  }
];
export const routes: Routes = [
  {path: 'article/:id', component: ArticleShow},
  {path: 'article/review/:id', component: ArticleReview},
  {path: 'article/edit/:id', component: ArticleEdit},
  ...routesDict.map((elem) => elem.route)
];
