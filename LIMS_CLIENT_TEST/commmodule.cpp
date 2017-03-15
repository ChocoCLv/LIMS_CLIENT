#include "commmodule.h"

#include <QDebug>

CommModule::CommModule(QObject *parent) : QObject(parent)
{
    initSocket();
}

void CommModule::readServer()
{
    qDebug()<<"read server";
    QByteArray datagram;
    while(commSocket->hasPendingDatagrams()){
        datagram.resize(commSocket->pendingDatagramSize());
        commSocket->readDatagram(datagram.data(),datagram.size());
        emit getResponse(datagram);
    }
}

void CommModule::sendMessage(QByteArray data)
{
    commSocket->writeDatagram(data,QHostAddress::LocalHost,2222);
}

void CommModule::initSocket()
{
    commSocket = new QUdpSocket();
    commSocket->bind(3333);
    connect(commSocket,SIGNAL(readyRead()),this,SLOT(readServer()));
}
