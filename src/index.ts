import { ApolloServer } from 'apollo-server-express';
import { ApolloGateway, RemoteGraphQLDataSource } from '@apollo/gateway';
import express, { Request } from 'express';
import * as dotenv from 'dotenv';

dotenv.config();

const app = express();
const port = process.env.PORT || 4000;

const gateway = new ApolloGateway({
    serviceList: [
        {
            name: 'workflows',
            url: process.env.WORKFLOW_SEARCH_URL
        }
    ],
    buildService({ name, url }) {
        return new RemoteGraphQLDataSource({
            url,
            willSendRequest({ request, context }) {
                request.http.headers.set(
                    'authorization',
                    context.authorization
                );
            }
        });
    }
});

const server = new ApolloServer({
    gateway,
    // Disable subscriptions (not currently supported with ApolloGateway)
    subscriptions: false,
    context: ({ req }: {req: Request}) => ({
        authorization: req.headers?.authorization || '',
    })
});

server.applyMiddleware({ app });


app.listen(port, () =>
    console.log(`Server ready at http://localhost:${port}${server.graphqlPath}`)
);
