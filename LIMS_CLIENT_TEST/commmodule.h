#ifndef COMMMODULE_H
#define COMMMODULE_H

#include <QObject>
#include <QUdpSocket>
#include <QHostAddress>

class CommModule : public QObject
{
    Q_OBJECT
public:
    explicit CommModule(QObject *parent = 0);
    void sendMessage(QByteArray data);

private:
    QUdpSocket *commSocket;
    void initSocket();

signals:
    void getResponse(QByteArray resp);

public slots:
    void readServer();
};

#endif // COMMMODULE_H
