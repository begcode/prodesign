/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

import path from 'path';
import Koa2 from 'koa';
import KoaBody from 'koa-body';
import KoaStatic from 'koa-static2';
import MainRoutes from './routes/main-routes';
import ErrorRoutesCatch from './middleware/ErrorRoutesCatch';
import ErrorRoutes from './routes/error-routes';

const app = new Koa2();
const env = process.env.NODE_ENV || 'development'; // Current mode
const PORT = 3290;
app
  .use((ctx, next) => {
    ctx.set('Access-Control-Allow-Origin', '*');
    ctx.set('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept, x-lowcode-org, x-lowcode-mode');
    ctx.set('Access-Control-Allow-Methods', 'PUT, POST, GET, DELETE, OPTIONS');
    ctx.set('Access-Control-Allow-Credentials', true); // 允许带上 cookie
    return next();
  })
  .use(ErrorRoutesCatch())
  .use(KoaStatic('assets', path.resolve(__dirname, '../assets'))) // Static resource
  .use(
    KoaBody({
      multipart: true,
      parsedMethods: ['POST', 'PUT', 'PATCH', 'GET', 'HEAD', 'DELETE'], // parse GET, HEAD, DELETE requests
      formidable: {
        uploadDir: path.join(__dirname, '../assets/uploads/tmp'),
      },
      jsonLimit: '50mb',
      formLimit: '50mb',
      textLimit: '50mb',
    }),
  ) // Processing request
  .use(MainRoutes.routes())
  .use(MainRoutes.allowedMethods())
  .use(ErrorRoutes());

if (env === 'development') {
  // logger
  app.use((ctx, next) => {
    const start = new Date();
    return next().then(() => {
      const ms = new Date() - start;
    });
  });
}

app.listen(PORT);

export default app;
