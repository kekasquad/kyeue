# Generated by Django 3.1.7 on 2021-04-03 20:55

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('core', '0002_auto_20210313_1738'),
    ]

    operations = [
        migrations.AddField(
            model_name='user',
            name='is_teacher',
            field=models.BooleanField(default=False, editable=False, verbose_name='Flag if person is a teacher'),
        ),
    ]